package org.test;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.test.exceptions.GitAddException;
import org.test.exceptions.GitCommitException;
import org.test.exceptions.GitDiffException;
import org.test.exceptions.GitPushException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GitUtilsTest {
    @Mock
    private Git git;

    @InjectMocks
    private GitUtils gitUtils;

    @Test
    public void gitDiffEntries_shouldFilterExcludedFileExtensions() throws GitAPIException {
        //Arrange
        DiffCommand diffCommand = mock(DiffCommand.class);
        DiffEntry diffEntry1 = mock(DiffEntry.class);
        when(diffEntry1.getNewPath()).thenReturn("src/main/java/com/example/MyClass.java");
        DiffEntry diffEntry2 = mock(DiffEntry.class);
        when(diffEntry2.getNewPath()).thenReturn("README.md");
        DiffEntry diffEntry3 = mock(DiffEntry.class);
        when(diffEntry3.getNewPath()).thenReturn("config.yaml");

        List<DiffEntry> mockEntries = List.of(diffEntry1, diffEntry2, diffEntry3);

        when(git.diff()).thenReturn(diffCommand);
        when(diffCommand.setCached(true)).thenReturn(diffCommand);
        when(diffCommand.call()).thenReturn(mockEntries);

        Set<String> excludedExtensions = Set.of(".yaml");

        //Act
        List<DiffEntry> entries = gitUtils.gitDiffEntries(excludedExtensions);

        //Assert
        assertNotNull(entries);
        assertEquals(2, entries.size());
        assertNotEquals(mockEntries, entries);
        assertTrue(entries.contains(diffEntry1));
        assertTrue(entries.contains(diffEntry2));
        assertFalse(entries.contains(diffEntry3));


        verify(git, times(1)).diff();
        verify(diffCommand, times(1)).call();
    }

    @Test
    public void gitDiffEntries_shouldReturnUnfilteredList_givenNoValidFileExtensions() throws GitAPIException {
        //Arrange
        DiffCommand diffCommand = mock(DiffCommand.class);
        DiffEntry diffEntry1 = mock(DiffEntry.class);
        when(diffEntry1.getNewPath()).thenReturn("src/main/java/com/example/MyClass.java");
        DiffEntry diffEntry2 = mock(DiffEntry.class);
        when(diffEntry2.getNewPath()).thenReturn("README.md");
        DiffEntry diffEntry3 = mock(DiffEntry.class);
        when(diffEntry3.getNewPath()).thenReturn("config.yaml");

        List<DiffEntry> mockEntries = List.of(diffEntry1, diffEntry2, diffEntry3);

        when(git.diff()).thenReturn(diffCommand);
        when(diffCommand.setCached(true)).thenReturn(diffCommand);
        when(diffCommand.call()).thenReturn(mockEntries);

        Set<String> excludedExtensions = Set.of(".py");

        //Act
        List<DiffEntry> entries = gitUtils.gitDiffEntries(excludedExtensions);

        //Assert
        assertNotNull(entries);
        assertEquals(3, entries.size());
        assertEquals(mockEntries, entries);
        assertTrue(entries.contains(diffEntry1));
        assertTrue(entries.contains(diffEntry2));
        assertTrue(entries.contains(diffEntry3));


        verify(git, times(1)).diff();
        verify(diffCommand, times(1)).call();
    }

    @Test
    public void gitDiffEntries_shouldThrowGitDiffEx_onGitApiEx() throws GitAPIException {
        //Arrange
        DiffCommand diffCommand = mock(DiffCommand.class);
        when(git.diff()).thenReturn(diffCommand);
        when(diffCommand.setCached(true)).thenReturn(diffCommand);
        when(diffCommand.call()).thenThrow(new GitAPIException("Test error") {
        });
        Set<String> set = new HashSet<>();

        //Act & Assert
        var thrown = assertThrows(GitDiffException.class, () -> {
           gitUtils.gitDiffEntries(set);
        });
        assertTrue(thrown.getMessage().contains("A Git API error occurred"));
        verify(git, times(1)).diff();
        verify(diffCommand, times(1)).setCached(true);
        verify(diffCommand, times(1)).call();
    }

    @Test
    public void gitAdd_shouldReturnDirCache() throws GitAPIException {
        //Arrange
        AddCommand addCommand = mock(AddCommand.class);
        DirCache dirCache = mock(DirCache.class);

        when(git.add()).thenReturn(addCommand);
        when(addCommand.addFilepattern(anyString())).thenReturn(addCommand);
        when(addCommand.setUpdate(true)).thenReturn(addCommand);
        when(addCommand.call()).thenReturn(dirCache);

        //Act
        DirCache expected = gitUtils.gitAdd();

        //Assert
        assertNotNull(expected);
        assertEquals(dirCache, expected);

        verify(git, times(1)).add();
        verify(addCommand, times(1)).addFilepattern(anyString());
        verify(addCommand, times(1)).setUpdate(true);
        verify(addCommand, times(1)).call();

    }

    @Test
    public void gitAdd_shouldThrowGitAddEx_onGitApiEx() throws GitAPIException {
        //Arrange
        AddCommand addCommand = mock(AddCommand.class);

        when(git.add()).thenReturn(addCommand);
        when(addCommand.addFilepattern(anyString())).thenReturn(addCommand);
        when(addCommand.setUpdate(true)).thenReturn(addCommand);
        when(addCommand.call()).thenThrow(new GitAPIException("Test error") {
        });

        //Act & Assert
        var ex = assertThrows(GitAddException.class, () -> {
            gitUtils.gitAdd();
        });
        assertInstanceOf(GitAPIException.class, ex.getCause());
        assertTrue(ex.getMessage().contains("A Git API error occurred"));

        verify(git, times(1)).add();
        verify(addCommand, times(1)).addFilepattern(anyString());
        verify(addCommand, times(1)).setUpdate(true);
        verify(addCommand, times(1)).call();

    }


    @Test
    public void  gitCommit_shouldReturnRevCommit() throws GitAPIException {
        //Arrange
        CommitCommand commitCommand = mock(CommitCommand.class);
        RevCommit revCommit = mock(RevCommit.class);
        String message = "I committed smth lol";

        when(git.commit()).thenReturn(commitCommand);
        when(commitCommand.setMessage(message)).thenReturn(commitCommand);
        when(commitCommand.call()).thenReturn(revCommit);

        //Act
        RevCommit expected = gitUtils.gitCommit(message);

        //Assert
        assertNotNull(expected);
        assertEquals(revCommit, expected);

        verify(git, times(1)).commit();
        verify(commitCommand, times(1)).setMessage(message);
        verify(commitCommand, times(1)).call();
    }

    @Test
    public void  gitCommit_shouldThrowGitCommitEx_onGitApiEx() throws GitAPIException {
        //Arrange
        CommitCommand commitCommand = mock(CommitCommand.class);
        String message = "I committed smth lol";

        when(git.commit()).thenReturn(commitCommand);
        when(commitCommand.setMessage(message)).thenReturn(commitCommand);
        when(commitCommand.call()).thenThrow(new GitAPIException("Test Error") {
        });

        //Act && Assert
        var ex =assertThrows(GitCommitException.class, () ->
                gitUtils.gitCommit(message)
        );
        assertInstanceOf(GitAPIException.class, ex.getCause());
        assertTrue(ex.getMessage().contains("A Git API error occurred"));

        verify(git, times(1)).commit();
        verify(commitCommand, times(1)).setMessage(message);
        verify(commitCommand, times(1)).call();
    }

    @Test
    void gitPush_shouldSuccessfullyPushChanges() throws GitAPIException {
        // Arrange
        PushCommand pushCommand = mock(PushCommand.class);
        String patToken = "pat-token";

        try(MockedStatic<FileUtils> utils = Mockito.mockStatic(FileUtils.class)){
            utils.when(FileUtils::extractPATToken).thenReturn(patToken);
        }

        when(git.push()).thenReturn(pushCommand);
        when(pushCommand.setCredentialsProvider(any(UsernamePasswordCredentialsProvider.class))).thenReturn(pushCommand);

        // Act
        gitUtils.gitPush();

        // Assert
        verify(git, times(1)).push();
        verify(pushCommand, times(1)).setCredentialsProvider(any(UsernamePasswordCredentialsProvider.class));
        verify(pushCommand, times(1)).call();

    }

    @Test
    void gitPush_shouldThrowGitPushException_onGitAPIError() throws GitAPIException {
        // Arrange
        String patToken = "token";
        PushCommand pushCommand = mock(PushCommand.class);

        try(MockedStatic<FileUtils> utils = Mockito.mockStatic(FileUtils.class)){
            utils.when(FileUtils::extractPATToken).thenReturn(patToken);
        }

        when(git.push()).thenReturn(pushCommand);
        when(pushCommand.setCredentialsProvider(any(UsernamePasswordCredentialsProvider.class))).thenReturn(pushCommand);
        when(pushCommand.call()).thenThrow(new GitAPIException("Test Git API Push Error") {}); // Simulate GitAPIException

        // Act & Assert
        GitPushException thrown = assertThrows(GitPushException.class, () -> {
            gitUtils.gitPush();
        });

        assertTrue(thrown.getMessage().contains("A Git API error occurred"));
        assertInstanceOf(GitAPIException.class, thrown.getCause());

    }

}